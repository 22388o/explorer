package org.royllo.explorer.core.configuration;

import org.royllo.explorer.core.util.parameters.MempoolParameters;
import org.royllo.explorer.core.util.parameters.TAPDParameters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Parameters configuration.
 */
@Configuration
@EnableConfigurationProperties({
        // Mempool configuration.
        MempoolParameters.class,
        MempoolParameters.Api.class,
        // TAPD configuration.
        TAPDParameters.class,
        TAPDParameters.Api.class
})
public class ParametersConfiguration {
}
