package cn.zhumingwu.consul.lock;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.agent.model.NewCheck;
import com.ecwid.consul.v1.kv.model.PutParams;
import com.ecwid.consul.v1.session.model.NewSession;
import com.ecwid.consul.v1.session.model.Session;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 基于Consul的互斥锁
 */
@Slf4j
public class ConsulLock implements Lock {
    static final String ttlSeconds = "30s";
    final ConsulClient consulClient;
    final String lockKey;
    final String checkName;
    final String sessionName;
    String sessionId = null;

    /**
     * @param consulClient
     * @param lockKey      同步锁在consul的KV存储中的Key路径，会自动增加prefix前缀，方便归类查询
     */
    public ConsulLock(ConsulClient consulClient, String lockKey) {
        this.consulClient = consulClient;
        this.lockKey = String.format("lock:%s", lockKey);
        this.checkName = String.format("check %s", lockKey);
        this.sessionName = String.format("session %s", lockKey);
    }

    @Override
    public void lock() {
        try {
            this.lockInterruptibly();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        tryLock(0L, TimeUnit.SECONDS);
    }

    @Override
    public boolean tryLock() {
        try {
            this.lockInterruptibly();
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long current = System.currentTimeMillis();
        long ttl = unit.toMillis(time);
        while (ttl >= 0) {
            try {
                this.sessionId = this.createSession();
                if (Strings.isNullOrEmpty(sessionId)) {
                    Thread.sleep(0);
                    continue;
                }
                PutParams putParams = new PutParams();
                putParams.setAcquireSession(this.sessionId);
                if (consulClient.setKVValue("lock:" + this.lockKey, current + ttl + " millis", putParams).getValue()) {
                    return true;
                }
            } finally {
                ttl = System.currentTimeMillis() - current;
            }
        }
        return false;
    }

    @Override
    public void unlock() {
        if (!Strings.isNullOrEmpty(this.sessionId)) {
            this.consulClient.sessionDestroy(this.sessionId, QueryParams.DEFAULT);
            this.sessionId = null;
        }
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    private String createSession() {
        NewCheck check = new NewCheck();
        check.setId(this.checkName);
        check.setName(check.getId());
        check.setTtl(ttlSeconds); //该值和session ttl共同决定决定锁定时长
        check.setTimeout("10s");
        consulClient.agentCheckRegister(check);
        consulClient.agentCheckPass(check.getId());

        NewSession session = new NewSession();
        session.setBehavior(Session.Behavior.RELEASE);
        session.setName(this.sessionName);
        session.setLockDelay(1);
        session.setTtl(ttlSeconds); //和check ttl共同决定锁时长
        List<String> checks = new ArrayList<>();
        checks.add(check.getId());
        session.setChecks(checks);

        String sessionId = consulClient.sessionCreate(session, QueryParams.DEFAULT).getValue();
        return sessionId;
    }
}
