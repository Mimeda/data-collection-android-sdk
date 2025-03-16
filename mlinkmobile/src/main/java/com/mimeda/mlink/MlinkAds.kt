package com.mimeda.mlink

import com.mimeda.mlink.common.MlinkConstants.CLICK
import com.mimeda.mlink.common.MlinkConstants.IMPRESSION
import com.mimeda.mlink.common.MlinkUrlBuilder
import com.mimeda.mlink.data.MlinkAdPayload
import com.mimeda.mlink.data.UrlPath
import com.mimeda.mlink.network.MlinkClient

object MlinkAds {
    /**
     * Sends an impression event to the server
     */
    suspend fun impression(payload: MlinkAdPayload) = apiCall(payload, UrlPath.IMPRESSION, IMPRESSION)
    /**
     * Sends a click event to the server
     */
    suspend fun click(payload: MlinkAdPayload) = apiCall(payload, UrlPath.CLICK, CLICK)
    /**
     * Executes an API call for the given ad event type
     */
    private suspend fun apiCall(payload: MlinkAdPayload, urlPath: UrlPath, eventType: String) {
        MlinkClient.get(MlinkUrlBuilder.prepareAdUrl(payload, urlPath), eventType)
    }
}