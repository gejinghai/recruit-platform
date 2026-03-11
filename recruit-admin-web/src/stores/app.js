import { defineStore } from 'pinia'
import { ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'

export const useAppStore = defineStore('app', () => {
  const locale = ref(localStorage.getItem('locale') || 'zhCn')
  const theme = ref(localStorage.getItem('theme') || 'light')

  const setLocale = (newLocale) => {
    locale.value = newLocale
    localStorage.setItem('locale', newLocale)
  }

  const setTheme = (newTheme) => {
    theme.value = newTheme
    localStorage.setItem('theme', newTheme)
    document.documentElement.setAttribute('data-theme', newTheme)
  }

  // 初始化主题
  const initTheme = () => {
    document.documentElement.setAttribute('data-theme', theme.value)
  }

  return {
    locale,
    theme,
    setLocale,
    setTheme,
    initTheme
  }
})
