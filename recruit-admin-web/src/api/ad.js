import request from './index'

export function getAdPositions() {
  return request({
    url: '/admin/ad/position/list',
    method: 'get'
  })
}

export function getAdContentList(params) {
  return request({
    url: '/admin/ad/content/list',
    method: 'get',
    params
  })
}

export function saveAdContent(data) {
  return request({
    url: '/admin/ad/content/save',
    method: 'post',
    data
  })
}

export function deleteAdContent(id) {
  return request({
    url: `/admin/ad/content/${id}`,
    method: 'delete'
  })
}
