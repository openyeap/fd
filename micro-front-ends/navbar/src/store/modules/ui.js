// state
const state = {
  mark: false,
  curFrame: '',
  themes: 'normal',
  panelStatus: [1,0],
  isHandle:false,
}

// actions
const actions = {

}

// getters
const getters = {

}

// mutations
const mutations = {
  setThemes(state, status) {
    state.themes = status;
  },
  setCurFrame(state, status) {
    state.curFrame = status;
  },
  setPanelStatus(state, status) {
    state.panelStatus = status
  },
  setMark(state, status){
    state.mark = status;
  },
  setIsHandle(state, status){
    state.isHandle = status;
  },
}

export default {
  state,
  getters,
  actions,
  mutations
}
