package org.royllo.explorer.web.controller.asset;

import lombok.RequiredArgsConstructor;
import org.royllo.explorer.core.dto.asset.AssetDTO;
import org.royllo.explorer.core.service.asset.AssetService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.royllo.explorer.web.configuration.WebConfiguration.DEFAULT_PAGE_SIZE;
import static org.royllo.explorer.web.util.constants.PagesConstants.SEARCH_PAGE;

/**
 * Search asset controller.
 */
@Controller
@RequiredArgsConstructor
public class SearchAssetController {

    /**
     * Asset service.
     */
    private final AssetService assetService;

    /**
     * Search result.
     *
     * @param model model
     * @param query query
     * @param page page number
     * @return page to display
     */
    @GetMapping("/search")
    public String home(final Model model,
                       @RequestParam(required = false) final Optional<String> query,
                       @RequestParam(defaultValue = "1") final int page) {
        // If the query is present, we make the search.
        if (query.isPresent()) {
            // Value the user searched for.
            model.addAttribute("query", query.get());
            // Adding result to the page.
            Page<AssetDTO> result = assetService.queryAssets(query.get(), page, DEFAULT_PAGE_SIZE);
            model.addAttribute("result", result);
        }
        return SEARCH_PAGE;
    }

}
