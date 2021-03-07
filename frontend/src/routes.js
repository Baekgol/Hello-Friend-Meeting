import Login from '@/views/user/Login.vue';
import Join from '@/views/user/Join.vue';
import FeedMain from '@/views/IndexFeed.vue';
import Community from '@/views/Community.vue';
import Profile from '@/views/user/Profile.vue';
import GroupMainPage from '@/components/group/GroupMainPage.vue';
import GroupBoardDetail from '@/components/group/GroupBoardDetail.vue';
import Message from '@/views/Message.vue';
import ErrorPage from '@/views/Error.vue';
import PageNotFound from '@/views/PageNotFound.vue';
import Naegi from '@/components/group/NaegiTab.vue';
import FriendProfile from '@/views/FriendProfile.vue';

export default [
  {
    path: '/naegi',
    name: 'naegi',
    component: Naegi,
  },
  {
    path: '/',
    name: 'Login',
    component: Login,
  },
  {
    path: '/user/join',
    name: 'Join',
    component: Join,
  },
  {
    path: '/user/profile',
    name: 'Profile',
    component: Profile,
  },
  {
    path: '/friendProfile',
    name: 'FriendProfile',
    component: FriendProfile,
    props: true,
  },
  {
    path: '/feed/main',
    name: 'FeedMain',
    component: FeedMain,
  },
  {
    path: '/community',
    name: 'Community',
    component: Community,
  },
  {
    path: '/message',
    name: 'Message',
    component: Message,
  },
  {
    path: '/group',
    name: 'GroupMainPage',
    component: GroupMainPage,
    props: true,
  },

  {
    path: '/error',
    name: 'ErrorPage',
    component: ErrorPage,
  },
  {
    path: '/board/detail',
    name: 'GroupBoardDetail',
    component: GroupBoardDetail,
  },
  {
    path: '*',
    name: 'PageNotFound',

    component: PageNotFound,
  },
];
