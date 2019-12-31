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
import VueRouter, {RouterOptions} from 'vue-router';
import {RouteConfig} from 'vue-router/types/router';
import {AuthModule} from '@/store/modules/auth';
import {refreshToken} from '@/api/auth';

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
        component: () => import(/* webpackChunkName: "login" */ '@/views/Login.vue'),
        meta: {
            requiresAuth: false,
        },
    },
    {
        path: '/register',
        name: 'register',
        component: () => import(/* webpackChunkName: "register" */ '@/views/Register.vue'),
        meta: {
            requiresAuth: false,
        },
    },
    {
        path: '/profile',
        name: 'profile',
        component: () => import(/* webpackChunkName: "profile_viewer" */ '@/views/Profile.vue'),
        meta: {
            requiresAuth: true,
        },
    },
    {
        path: '/movies',
        name: 'movies',
        component: () => import(/* webpackChunkName: "movies" */ '@/views/Movies.vue'),
        children: [{
            path: ':id',
            name: 'view-movie',
            // TODO
        }],
    },
];

const router = new VueRouter({
    routes,
    mode: 'history',
    base: process.env.BASE_URL,
    linkExactActiveClass: 'is-active',
} as RouterOptions);

router.beforeEach(async (to, from, next) => {
    if (!from.name) try {
        await refreshToken();
    } catch (e) {
    }

    const requiresAuth = to.meta.requiresAuth;
    if (requiresAuth !== undefined && requiresAuth !== AuthModule.isAuthenticated) {
        return next({name: 'home'});
    }

    next();
});

export default router;
