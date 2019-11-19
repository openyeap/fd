package ltd.fdsa.fds.core;

import java.util.concurrent.Callable;

import ltd.fdsa.fds.core.*;
import ltd.fdsa.fds.core.config.Configuration; 

 

public class ReaderWorker implements Callable<Integer> {

    private final Reader reader;
    private final JobContext context;
    private final RecordCollector rc;
    private final Configuration readerConfig;

    public ReaderWorker(Reader reader, JobContext context, Configuration readerConfig, RecordCollector rc) {
        this.reader = reader;
        this.context = context;
        this.rc = rc;
        this.readerConfig = readerConfig;
    }

    @Override
    public Integer call() throws Exception {
        Thread.currentThread().setContextClassLoader(reader.getClass().getClassLoader());
        reader.prepare(context, readerConfig);
        reader.execute(rc);
        reader.close();
        return 0;
    }

}
