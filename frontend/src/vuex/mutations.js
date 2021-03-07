export default {
  setIsHeader: function(state, payload) {
    state.isHeader = payload;
  },
  setIsFooter: function(state, payload) {
    state.isFooter = payload;
  },
  setGno: function(state, payload) {
    state.vuexGno = payload;
  },
  setUno: function(state, payload) {
    state.vuexUno = payload;
  },
  setMemberStatus: function(state, payload) {
    state.vuexMemberStatus = payload;
  },

  OPEN_CALENDAR_DIALOG(state, payload) {
    state.calendar.startDate = payload.date;
    state.calendar.startTime = payload.time;
    state.calendar.hasTime = payload.hasTime;
    state.calendar.endDate = payload.date;
    state.calendar.endTime = '';
    state.calendar.title = '';
    state.calendar.content = '';
    state.dialog = true;
  },
  CLOSE_CALENDAR_DIALOG(state) {
    state.calendar.stitle = '';
    state.calendar.scontent = '';
    state.dialog = false;
  },
  ADD_EVENTS(state, event) {
    state.events = [];
    for (var i = 0; i < event.length; i++) {
      state.events.push(makeEvent(event[i]));
      // alert(state.events[i].color)
    }
  },
  ADD_EVENT(state, getEvent) {
    state.events.push(makeEvent(getEvent));
    state.dialog = false;
    state.event = initEvent();
  },
  setBno: function(state, payload) {
    state.vuexBno = payload;
  },
  setIsWriter: function(state, payload) {
    state.isWriter = payload;
  },
  setFeedCreatedTime: function(state) {
    state.feedCreatedTime++;
  },
  resetFeedCreatedTime: function(state) {
    state.feedCreatedTime = 0;
  },
  setTabNum: function(state, payload) {
    state.tabNum = payload;
  },
  setChatPageH: function(state, payload) {
    state.chatPageH = payload;
  },
  setRouteUrl: function(state, payload) {
    state.routeUrl = payload;
  },
};

const colors = ['blue', 'indigo', 'deep-purple', 'green', 'orange', 'red'];

const makeEvent = (event) => {
  return {
    name: event.stitle,
    start: event.sstartdate,
    end: event.senddate,
    color: colors[Math.floor(Math.random() * 6)],
    content: event.scontent,
    smasterEmail: event.smasterEmail,
    sno: event.sno,
  };
};

function initEvent() {
  return {
    startDate: '',
    startTime: '',
    endDate: '',
    endTime: '',
    content: '',
    title: '',
  };
}
