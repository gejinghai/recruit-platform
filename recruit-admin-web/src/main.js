import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import i18n from './i18n'

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

const savedLocale = localStorage.getItem('locale') || 'zhCn'

// 先设置语言
i18n.global.locale.value = savedLocale

// 使用插件
app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: savedLocale === 'zhCn' ? zhCn : en
})
app.use(i18n)

app.mount('#app')
