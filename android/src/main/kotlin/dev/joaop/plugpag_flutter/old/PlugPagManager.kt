package dev.joaop.plugpag_flutter.old

import android.content.Context
import br.com.uol.pagseguro.plugpag.PlugPag
import br.com.uol.pagseguro.plugpag.PlugPagAppIdentification
import dev.joaop.plugpag_flutter.R

class PlugPagManager private constructor(appContext: Context) {
    /**
     * Returns the PlugPag reference.
     *
     * @return PlugPag reference.
     */
    // -----------------------------------------------------------------------------------------------------------------
    // Instance attributes
    // -----------------------------------------------------------------------------------------------------------------
    var plugPag: PlugPag? = null
    // -----------------------------------------------------------------------------------------------------------------
    // Constructors
    // -----------------------------------------------------------------------------------------------------------------
    /**
     * Creates a new PlugPagManager instance.<br></br>
     * This instance is meant to be singleton.
     *
     * @param appContext Application reference.
     */
    init {
        val appIdentification: PlugPagAppIdentification?
        appIdentification = PlugPagAppIdentification(
                appContext.getString(R.string.plugpag_app_identification),
                appContext.getString(R.string.plugpag_app_version))
        plugPag = PlugPag(appContext, appIdentification)
    }

    // -----------------------------------------------------------------------------------------------------------------
    // PlugPag accessor
    // -----------------------------------------------------------------------------------------------------------------
    companion object {
        // -----------------------------------------------------------------------------------------------------------------
        // Class attributes
        // -----------------------------------------------------------------------------------------------------------------
        private var sInstance: PlugPagManager? = null
        // -----------------------------------------------------------------------------------------------------------------
        // Singleton accessor
        // -----------------------------------------------------------------------------------------------------------------
        /**
         * Creates a new PlugPagManager instance if none exists.
         *
         * @param appContext Application reference.
         * @return PlugPagManager singleton reference.
         */
        fun create(appContext: Context): PlugPagManager? {
            if (sInstance == null) {
                sInstance = PlugPagManager(appContext)
            }
            return sInstance
        }

        /**
         * Returns the PlugPagManager singleton reference.
         *
         * @return PlugPagManager singleton reference.
         */
        val instance: PlugPagManager?
            get() {
                if (sInstance == null) {
                    throw RuntimeException("PlugPagManager not instantiated")
                }
                return sInstance
            }
    }
}