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
        
        assertTrue(emotionIds.contains(1L))
        assertTrue(emotionIds.contains(2L))
        assertTrue(emotionIds.contains(3L))
        assertTrue(emotionIds.contains(5L))
    }

    @Test
    fun getDefaultEmotionById_returnsCorrectEmotion() {
        val happyEmotion = Emotion.getDefaultEmotionById(1L)
        
        assertNotNull(happyEmotion)
        assertEquals(1L, happyEmotion!!.id)
        assertEquals("😊", happyEmotion.emoji)
        assertFalse(happyEmotion.isUserCreated)
    }

    @Test
    fun createUserEmotion_createsCorrectEmotion() {
        val emotion = Emotion.createUserEmotion(
            name = "测试情绪",
            emoji = "🎯",
            description = "这是一个测试情绪",
            id = 123L
        )
        
        assertEquals(123L, emotion.id)
        assertEquals("测试情绪", emotion.name)
        assertEquals("🎯", emotion.emoji)
        assertEquals("这是一个测试情绪", emotion.description)
        assertTrue(emotion.isUserCreated)
    }

    @Test
    fun allDefaultEmotions_haveValidData() {
        val emotions = Emotion.getDefaultEmotions()
        
        emotions.forEach { emotion ->
            assertTrue("ID should be positive", emotion.id > 0)
            assertTrue("Name should not be blank", emotion.name.isNotBlank())
            assertTrue("Emoji should not be blank", emotion.emoji.isNotBlank())
            assertFalse("Should not be user created", emotion.isUserCreated)
        }
    }
}
