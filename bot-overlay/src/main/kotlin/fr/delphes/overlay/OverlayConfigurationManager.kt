package fr.delphes.overlay

import fr.delphes.bot.connector.ConfigurationManager

class OverlayConfigurationManager : ConfigurationManager<OverlayConfiguration> {
    override val configuration = OverlayConfiguration()

    override suspend fun configure(newConfiguration: OverlayConfiguration) {

    }
}