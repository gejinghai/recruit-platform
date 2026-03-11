import request from './index'

export function getJobList(params) {
  return request({
    url: '/admin/job/list',
    method: 'get',
    params
  })
}

export function addJob(data) {
  return request({
    url: '/admin/job/add',
    method: 'post',
    data
  })
}

export function updateJob(data) {
  return request({
    url: '/admin/job/update',
    method: 'post',
    data
  })
}

export function deleteJob(id) {
  return request({
    url: `/admin/job/${id}`,
    method: 'delete'
  })
}

export function topJob(id, isTop) {
  return request({
    url: `/admin/job/top/${id}`,
    method: 'post',
    params: { isTop }
  })
}
