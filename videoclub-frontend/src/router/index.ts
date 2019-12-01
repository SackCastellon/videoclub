/*
 * Copyright 2019 Juan José González Abril
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
import VueRouter from 'vue-router';
import {RouteConfig} from 'vue-router/types/router';
import {UserModule} from '@/store/modules/user';

Vue.use(VueRouter);

const routes: RouteConfig[] = [
    {
        path: '/',
        name: 'home',
        component: () => import(/* webpackChunkName: "home" */ '@/views/Home.vue'),
    },
    {
        path: '/login',
        name: 'login',
        meta: {
            requiresNoAuth: true,
        },
        component: () => import(/* webpackChunkName: "login" */ '@/views/Login.vue'),
    },
    {
        path: '/register',
        name: 'register',
        meta: {
            requiresNoAuth: true,
        },
        component: () => import(/* webpackChunkName: "register" */ '@/views/Register.vue'),
    },
];

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
});

router.beforeEach(async (to, from, next) => {
    if (to.meta.requiresNoAuth) {
        await UserModule.refresh();

        if (UserModule.isAuthenticated) {
            return next({name: 'home'});
        }
    }

    next();
});

export default router;
