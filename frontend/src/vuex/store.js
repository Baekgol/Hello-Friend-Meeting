import Vue from 'vue';
import Vuex from 'vuex';
import getters from './getters';
import actions from './actions';
import mutations from './mutations';

Vue.use(Vuex);

const state = {
  isHeader: false,
  isFooter: false,
  calendar: initEvent(),
  events: [],
  dialog: false,
  vuexGno: { type: Number },
  vuexUno: { type: Number },
  vuexMemberStatus: { type: Number },
  vuexBno: { type: Number },
  isWriter: { type: Boolean },
  feedCreatedTime: 0,
  tabNum: 0,
  chatPageH: 0,
  routeUrl: null,
};
function initEvent() {
  return {
    sno: { type: Number },
    sgno: { type: Number },
    smaster: { type: Number },
    senddate: '',
    sstartdate: '',
    stitle: '',
    sspace: '',
    scontent: '',
  };
}
export default new Vuex.Store({
  state,
  mutations,
  getters,
  actions,
});
