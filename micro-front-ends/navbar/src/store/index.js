import Vue from 'vue';
import Vuex from 'vuex';
import ui from './modules/ui';
import dict from './modules/dict';
import video from './modules/video';
import map from './modules/map';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {},
  mutations: {},
  modules: {
    ui,
    map,
    dict,
    video
  }
})
