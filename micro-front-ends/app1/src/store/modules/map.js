// state
const state = {
  map: null,
  mapTileLayer:{
    normal:'',
    dark:''
  }
}

// actions
const actions = {

}

// getters
const getters = {

}

// mutations
const mutations = {
  setMap(state, status) {
    state.map = status;
  },
  setMapTileLayer(state, status){
    state.mapTileLayer = status;
  }
}


export default {
  state,
  getters,
  actions,
  mutations
}
