<template>
  <div class="password">
    <el-card>
      <template #header>
        <span>{{ $t('password.title') }}</span>
      </template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" style="width: 400px">
        <el-form-item :label="$t('password.oldPassword')" prop="oldPassword">
          <el-input v-model="form.oldPassword" type="password" />
        </el-form-item>
        <el-form-item :label="$t('password.newPassword')" prop="newPassword">
          <el-input v-model="form.newPassword" type="password" />
        </el-form-item>
        <el-form-item :label="$t('password.confirmPassword')" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const formRef = ref()

const form = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirm = (rule, value, callback) => {
  if (value !== form.value.newPassword) {
    callback(new Error('Passwords do not match'))
  } else {
    callback()
  }
}

const rules = {
  oldPassword: [{ required: true, message: '', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '', trigger: 'blur' },
    { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ]
}

const handleSubmit = async () => {
  await formRef.value.validate()
  ElMessage.success(t('password.success'))
  form.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
}
</script>

<style scoped>
.password {
  padding: 20px;
}
</style>
