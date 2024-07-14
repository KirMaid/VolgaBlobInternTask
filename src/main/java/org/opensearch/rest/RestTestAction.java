package org.opensearch.rest;

import org.opensearch.client.node.NodeClient;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class RestTestAction extends BaseRestHandler {

    @Override
    public String getName() {
        return "rest_handler_test";
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient nodeClient) throws IOException {
        return null;
    }

    @Override
    public List<Route> routes() {
        return Collections.singletonList(
                new Route(RestRequest.Method.GET, "/_plugins/statistics")
        );
    }
}
