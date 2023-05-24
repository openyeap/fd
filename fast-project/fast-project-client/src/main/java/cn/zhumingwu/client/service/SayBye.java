package cn.zhumingwu.client.service;

public class SayBye extends SayService {
    @Override
    public void say(String message) {
        super.say("Bye,Bye, " + message);
    }
}
