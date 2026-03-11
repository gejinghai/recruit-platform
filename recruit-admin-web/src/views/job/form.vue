<template>
  <div class="job-form">
    <el-card>
      <template #header>
        <span>{{ isEdit ? $t('job.editJob') : $t('job.addJob') }}</span>
      </template>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('job.title')" prop="title">
              <el-input v-model="form.title" :placeholder="$t('job.title')" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('job.companyName')" prop="companyName">
              <el-input v-model="form.companyName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('job.salary')" prop="salary">
              <el-input v-model="form.salary" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('job.workType')" prop="workType">
              <el-select v-model="form.workType" :placeholder="$t('job.workType')">
                <el-option :label="$t('common.all')" value="" />
                <el-option label="长白班" value="长白班" />
                <el-option label="夜班" value="夜班" />
                <el-option label="两班倒" value="两班倒" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('job.workTime')">
              <el-input v-model="form.workTime" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('job.collectionTime')">
              <el-input v-model="form.collectionTime" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('job.address')">
              <el-input v-model="form.address" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('common.status')">
              <el-radio-group v-model="form.status">
                <el-radio :label="1">{{ $t('common.online') }}</el-radio>
                <el-radio :label="0">{{ $t('common.offline') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('job.contactType')">
              <el-radio-group v-model="form.contactType">
                <el-radio label="text">{{ $t('job.text') }}</el-radio>
                <el-radio label="image">{{ $t('job.image') }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('job.contactValue')">
              <el-input
                v-if="form.contactType === 'text'"
                v-model="form.contactValue"
                :placeholder="$t('job.contactValue')"
              />
              <el-input
                v-else
                v-model="form.contactValue"
                :placeholder="$t('ad.imageUrl')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('job.contactPreview')" v-if="form.contactValue">
          <div style="color: #999; font-size: 12px; margin-bottom: 10px;">
            <span v-if="form.contactType === 'text'">Preview: {{ form.contactValue }}</span>
            <span v-else>Image Preview:</span>
          </div>
          <el-image
            v-if="form.contactType === 'image' && form.contactValue"
            :src="form.contactValue"
            style="width: 150px; height: 150px"
            fit="contain"
          />
        </el-form-item>
        <el-form-item :label="$t('job.welfare')">
          <el-checkbox-group v-model="welfareList">
            <el-checkbox label="包住" />
            <el-checkbox label="包吃" />
            <el-checkbox label="日结" />
            <el-checkbox label="夜班补贴" />
            <el-checkbox label="可接收大龄工" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item :label="$t('job.description')">
          <el-input v-model="form.description" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item :label="$t('job.requirements')">
          <el-input v-model="form.requirements" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">{{ $t('common.save') }}</el-button>
          <el-button @click="$router.back()">{{ $t('common.cancel') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getJobList, addJob, updateJob } from '../../api/job'
import { ElMessage } from 'element-plus'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
const route = useRoute()
const router = useRouter()

const formRef = ref()
const isEdit = computed(() => !!route.params.id)
const welfareList = ref([])

const form = ref({
  id: null,
  title: '',
  companyName: '',
  salary: '',
  workType: '',
  workTime: '',
  collectionTime: '',
  address: '',
  status: 1,
  description: '',
  requirements: '',
  welfare: '',
  contactType: 'text',
  contactValue: ''
})

const rules = {
  title: [{ required: true, message: '', trigger: 'blur' }],
  companyName: [{ required: true, message: '', trigger: 'blur' }],
  salary: [{ required: true, message: '', trigger: 'blur' }]
}

const loadData = async () => {
  if (!isEdit.value) return
  try {
    const res = await getJobList()
    const job = (res.data || []).find(j => j.id === Number(route.params.id))
    if (job) {
      form.value = { ...job }
      if (job.welfare) {
        try {
          welfareList.value = JSON.parse(job.welfare)
        } catch (e) {
          welfareList.value = []
        }
      }
    }
  } catch (e) {
    console.error(e)
  }
}

const handleSubmit = async () => {
  await formRef.value.validate()
  form.value.welfare = JSON.stringify(welfareList.value)
  try {
    if (isEdit.value) {
      await updateJob(form.value)
      ElMessage.success(t('job.saveSuccess'))
    } else {
      await addJob(form.value)
      ElMessage.success(t('job.saveSuccess'))
    }
    router.push('/job')
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.job-form {
  padding: 20px;
}
</style>
