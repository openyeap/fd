package ltd.fdsa.switcher.core.job.coordinator;

import ltd.fdsa.switcher.core.job.coordinator.Coordinator;
import ltd.fdsa.switcher.core.job.model.HandleCallbackParam;
import ltd.fdsa.switcher.core.job.model.RegistryParam;
import ltd.fdsa.switcher.core.util.JobRemotingUtil;
import ltd.fdsa.web.view.Result;

import java.util.List;

/**
 * executor invoke coordinator api
 */
public class CoordinatorClient implements Coordinator {

    private String addressUrl;
    private String accessToken;

    public CoordinatorClient() {
    }

    public CoordinatorClient(String addressUrl, String accessToken) {
        this.addressUrl = addressUrl;
        this.accessToken = accessToken;

        // valid
        if (!this.addressUrl.endsWith("/")) {
            this.addressUrl = this.addressUrl + "/";
        }
    }

    @Override
    public Result<String> callback(List<HandleCallbackParam> callbackParamList) {
        return JobRemotingUtil.postBody(addressUrl + "api/callback", accessToken, callbackParamList, 3);
    }

    @Override
    public Result<String> registry(RegistryParam registryParam) {
        return JobRemotingUtil.postBody(addressUrl + "api/registry", accessToken, registryParam, 3);
    }

    @Override
    public Result<String> registryRemove(RegistryParam registryParam) {
        return JobRemotingUtil.postBody(
                addressUrl + "api/registryRemove", accessToken, registryParam, 3);
    }
}
