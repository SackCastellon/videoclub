/*
 * Copyright 2020 Juan José González Abril
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';

import Buefy from 'buefy';
import 'buefy/dist/buefy.min.css';
// @ts-ignore
import config from 'buefy/src/utils/config';
// @ts-ignore
import VueCarousel from 'vue-carousel';

import '@mdi/font/css/materialdesignicons.min.css';
import '@/assets/scss/bootstrap.scss';
import {dateFormat, moment} from '@/util/Moment';

Vue.config.productionTip = false;

Vue.use(Buefy);

// FIXME Workaround
config.defaultTooltipAnimated = true;
config.defaultInputAutocomplete = 'off';
config.defaultDateFormatter = (date: Date) => moment(date).format(dateFormat);
config.defaultDateParser = (date: string) => moment(date, dateFormat).toDate();
config.defaultFirstDayOfWeek = 1;
config.defaultUseHtml5Validation = false;
config.defaultUseHtml5Validation = false;


Vue.use(VueCarousel);

Vue.directive('focus', {
    inserted: el => {
        const child = el.querySelector('input') || el.querySelector('button');
        if (child) child.focus();
    },
});

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app');
