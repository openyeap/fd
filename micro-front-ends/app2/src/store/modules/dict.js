'use strict'

// 初始化数据
const state = {
  systemDict: JSON.parse(sessionStorage.getItem('systemDict')), //字典表
}

// getters
const getters = {
  systemDict: state => state.systemDict,
}

// actions: 异步方法
const actions = {
  // 查询字典
  async getSystemDict ({ commit }, postData) {
    let params = await getData.getSystemDict(postData)
    sessionStorage.setItem('systemDict', JSON.stringify(params))
    commit('SET_SYSTEM_DICT', params)
  },
}
// mutations: 同步并且操作数据的方法
const mutations = {
  SET_SYSTEM_DICT (state, params) {
    state.systemDict = params
  },
}

export default {
  state,
  getters,
  actions,
  mutations
}
