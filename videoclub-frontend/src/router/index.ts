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
import {UserModule} from '@/store/modules/user';
import {UserType} from '@/data/User';

Vue.use(VueRouter);

export enum LoginMode {
    NONE,
    SOME,
    MEMBER,
    ADMIN,
}

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
            requiredLogin: LoginMode.NONE,
        },
    },
    {
        path: '/register',
        name: 'register',
        component: () => import(/* webpackChunkName: "register" */ '@/views/Register.vue'),
        meta: {
            requiredLogin: LoginMode.NONE,
        },
    },
    {
        path: '/profile',
        name: 'profile-view',
        component: () => import(/* webpackChunkName: "profile_viewer" */ '@/views/Profile.vue'),
        meta: {
            requiredLogin: LoginMode.SOME,
        },
    },
    {
        path: '/movie',
        name: 'movie-list',
        component: () => import(/* webpackChunkName: "movies" */ '@/views/Movies.vue'),
        children: [
            {
                path: 'new',
                name: 'movie-create',
                meta: {
                    requiredLogin: LoginMode.ADMIN,
                },
            },
            {
                path: ':id',
                name: 'movie-view',
                children: [
                    {
                        path: 'edit',
                        name: 'movie-edit',
                        meta: {
                            requiredLogin: LoginMode.ADMIN,
                        },
                    },
                ],
            },
        ],
    },
    {
        path: '/shop',
        name: 'shop-list',
        component: () => import(/* webpackChunkName: "shops" */ '@/views/Shops.vue'),
        children: [
            {
                path: 'new',
                name: 'shop-create',
                meta: {
                    requiredLogin: LoginMode.ADMIN,
                },
            },
            {
                path: ':id',
                name: 'shop-view',
                children: [
                    {
                        path: 'edit',
                        name: 'shop-edit',
                        meta: {
                            requiredLogin: LoginMode.ADMIN,
                        },
                    },
                ],
            },
        ],
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

    if (AuthModule.isAuthenticated && UserModule.data === null) {
        await UserModule.fetchUserData();
    }

    const mode = to.meta.requiredLogin as LoginMode | undefined;

    if (mode !== undefined) {
        if (mode === LoginMode.NONE) {
            // Current route requires user to be logged out
            if (AuthModule.isAuthenticated) {
                return next({name: 'home'});
            }
        } else {
            // Current route requires user to be logged in
            if (!AuthModule.isAuthenticated) {
                return next({name: 'login', query: {next: to.fullPath}});
            }

            // Current route requires user to be a MEMBER
            if (mode === LoginMode.MEMBER && UserModule.data?.type !== UserType.MEMBER) {
                return next({name: 'home'});
            }

            // Current route requires user to be an ADMIN
            if (mode === LoginMode.ADMIN && UserModule.data?.type !== UserType.ADMIN) {
                return next({name: 'home'});
            }
        }
    }

    next();
});

export default router;
