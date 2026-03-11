<template>
  <el-config-provider :locale="currentLocale === 'zhCn' ? zhCn : en">
    <el-container class="admin-layout">
      <el-aside width="200px">
        <div class="logo">{{ $t('login.title') }}</div>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>{{ $t('menu.dashboard') }}</span>
          </el-menu-item>
          <el-menu-item index="/job">
            <el-icon><Briefcase /></el-icon>
            <span>{{ $t('menu.job') }}</span>
          </el-menu-item>
          <el-menu-item index="/ad">
            <el-icon><Picture /></el-icon>
            <span>{{ $t('menu.ad') }}</span>
          </el-menu-item>
          <el-menu-item index="/password">
            <el-icon><Lock /></el-icon>
            <span>{{ $t('menu.password') }}</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-right">
            <el-select v-model="currentLocale" @change="handleLocaleChange" style="width: 100px; margin-right: 10px;">
              <el-option label="中文" value="zhCn" />
              <el-option label="English" value="en" />
            </el-select>
            <el-switch
              v-model="isDark"
              :active-action-icon="Moon"
              :inactive-action-icon="Sunny"
              @change="handleThemeChange"
              style="margin-right: 15px;"
            />
            <span class="username">{{ userStore.userInfo.username }}</span>
            <el-button type="danger" size="small" @click="handleLogout">{{ $t('menu.logout') }}</el-button>
          </div>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </el-config-provider>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../stores/user'
import { useAppStore } from '../stores/app'
import { logout } from '../api/auth'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import en from 'element-plus/es/locale/lang/en'
import { Moon, Sunny } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const appStore = useAppStore()
const { locale } = useI18n()

const activeMenu = computed(() => route.path)
const currentLocale = ref(appStore.locale)
const isDark = computed({
  get: () => appStore.theme === 'dark',
  set: (val) => appStore.setTheme(val ? 'dark' : 'light')
})

const handleLocaleChange = (val) => {
  appStore.setLocale(val)
  locale.value = val
}

const handleThemeChange = (val) => {
  appStore.setTheme(val ? 'dark' : 'light')
}

const handleLogout = async () => {
  await logout()
  userStore.logout()
  router.push('/login')
}

appStore.initTheme()
</script>

<style>
html[data-theme="dark"] {
  --el-bg-color: #1d1e1f;
  --el-bg-color-overlay: #262727;
  --el-text-color-primary: #e5eaf3;
  --el-border-color: #4c4d4f;
}

.admin-layout {
  height: 100vh;
}

.el-aside {
  background-color: #304156 !important;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  background-color: #2b3a4a;
}

.el-menu {
  border-right: none !important;
}

.el-header {
  background-color: var(--el-bg-color) !important;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.header-right {
  display: flex;
  align-items: center;
}

.username {
  color: var(--el-text-color-primary);
  margin-right: 15px;
}

.el-main {
  background-color: var(--el-bg-color) !important;
}
</style>
