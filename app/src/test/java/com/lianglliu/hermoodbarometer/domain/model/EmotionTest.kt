package com.lianglliu.hermoodbarometer.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Emotion 数据模型的单元测试
 */
class EmotionTest {

    @Test
    fun getDefaultEmotions_returnsCorrectCount() {
        val emotions = Emotion.getDefaultEmotions()
        assertEquals(10, emotions.size)
    }

    @Test
    fun getDefaultEmotions_containsExpectedEmotions() {
        val emotions = Emotion.getDefaultEmotions()
        val emotionIds = emotions.map { it.id }
        
        assertTrue(emotionIds.contains("happy"))
        assertTrue(emotionIds.contains("sad"))
        assertTrue(emotionIds.contains("angry"))
        assertTrue(emotionIds.contains("calm"))
    }

    @Test
    fun getDefaultEmotionById_returnsCorrectEmotion() {
        val happyEmotion = Emotion.getDefaultEmotionById("happy")
        
        assertNotNull(happyEmotion)
        assertEquals("happy", happyEmotion!!.id)
        assertEquals("😊", happyEmotion.emoji)
        assertFalse(happyEmotion.isCustom)
    }

    @Test
    fun fromCustomEmotion_createsCorrectEmotion() {
        val customEmotion = CustomEmotion(
            id = 123L,
            name = "测试情绪",
            emoji = "🎯",
            description = "这是一个测试情绪"
        )
        
        val emotion = Emotion.fromCustomEmotion(customEmotion)
        
        assertEquals("custom_123", emotion.id)
        assertEquals("测试情绪", emotion.name)
        assertEquals("🎯", emotion.emoji)
        assertEquals("这是一个测试情绪", emotion.description)
        assertTrue(emotion.isCustom)
        assertEquals(123L, emotion.customId)
    }

    @Test
    fun allDefaultEmotions_haveValidData() {
        val emotions = Emotion.getDefaultEmotions()
        
        emotions.forEach { emotion ->
            assertTrue("ID should not be blank", emotion.id.isNotBlank())
            assertTrue("Name should not be blank", emotion.name.isNotBlank())
            assertTrue("Emoji should not be blank", emotion.emoji.isNotBlank())
            assertFalse("Should not be custom", emotion.isCustom)
        }
    }
}
