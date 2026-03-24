package com.lianglliu.hermoodbarometer.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import jakarta.inject.Inject
import java.io.InputStream
import java.io.OutputStream

/** An [Serializer] for the [UserPreferences] proto. */
class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {

    override val defaultValue: UserPreferences = getCustomInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences =
        try {
            UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}

internal fun getCustomInstance() = userPreferences {
    colorSchemeConfig = ColorSchemeConfigProto.COLOR_SCHEME_CONFIG_WARM
}
