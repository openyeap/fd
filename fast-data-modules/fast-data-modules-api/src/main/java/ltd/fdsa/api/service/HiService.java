package ltd.fdsa.api.service;

//import ltd.fdsa.starter.remote.RpcApiClient;
//
//@RpcApiClient("${project.remote.service.name:${spring.application.name:}}")
public interface HiService {

    public String hi(String message);
}

