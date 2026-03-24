-repackageclasses

# Room - keep entity classes and their fields
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Proto DataStore - keep generated protobuf classes
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }

# kotlinx.serialization - keep serializable classes
-keepattributes *Annotation*, InnerClasses
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.lianglliu.hermoodbarometer.**$$serializer { *; }
-keepclassmembers class com.lianglliu.hermoodbarometer.** {
    *** Companion;
}
-keepclasseswithmembers class com.lianglliu.hermoodbarometer.** {
    kotlinx.serialization.KSerializer serializer(...);
}
