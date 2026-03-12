const BASE_URL = '/api'

export const request = (options) => {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: options.header || {},
      success: (res) => {
        if (res.data.code === 200) {
          resolve(res.data)
        } else {
          uni.showToast({
            title: res.data.message || '请求失败',
            icon: 'none'
          })
          reject(res.data)
        }
      },
      fail: (err) => {
        uni.showToast({
          title: '网络错误',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

export function getJobList(params) {
  return request({
    url: '/job/list',
    method: 'GET',
    data: params
  })
}

export function getJobDetail(id) {
  return request({
    url: `/job/${id}`,
    method: 'GET'
  })
}

export function getBanner() {
  return request({
    url: '/home/banner',
    method: 'GET'
  })
}
