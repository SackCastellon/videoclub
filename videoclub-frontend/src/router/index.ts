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
import VueRouter, {Location, Route, RouterOptions} from 'vue-router';
import {RouteConfig} from 'vue-router/types/router';
import {AuthModule} from '@/store/modules/auth';
import {refresh} from '@/api/modules/auth';
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
        component: () => import(/* webpackChunkName: "auth" */ '@/views/Auth.vue'),
        meta: {
            requiredLogin: LoginMode.NONE,
        },
    },
    {
        path: '/register',
        name: 'register',
        component: () => import(/* webpackChunkName: "auth" */ '@/views/Auth.vue'),
        meta: {
            requiredLogin: LoginMode.NONE,
        },
    },
    {
        path: '/account/profile',
        name: 'profile-view',
        component: () => import(/* webpackChunkName: "profile" */ '@/views/Profile.vue'),
        meta: {
            requiredLogin: LoginMode.SOME,
        },
        children: [
            {
                path: 'edit',
                name: 'profile-edit',
                meta: {
                    requiredLogin: LoginMode.SOME,
                },
            },
        ],
    },
    {
        path: '/account/rental',
        name: 'rental-list',
        component: () => import(/* webpackChunkName: "rentals" */ '@/views/Rentals.vue'),
        meta: {
            requiredLogin: LoginMode.MEMBER,
        },
        children: [
            {
                path: ':id',
                name: 'rental-view',
                meta: {
                    requiredLogin: LoginMode.MEMBER,
                },
                children: [
                    {
                        path: 'edit',
                        name: 'rental-edit',
                        meta: {
                            requiredLogin: LoginMode.MEMBER,
                        },
                    },
                ],
            },
        ],
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
    {
        path: '/cart',
        name: 'cart',
        component: () => import(/* webpackChunkName: "cart" */ '@/views/Cart.vue'),
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
        await refresh();
    } catch (e) {
    }

    const mode = to.meta.requiredLogin as LoginMode | undefined;
    const userType = UserModule.user?.type;

    if (mode !== undefined) {
        if (!AuthModule.isAuthenticated) {
            if (mode !== LoginMode.NONE) {
                return next({name: 'login', query: {next: to.fullPath}});
            }
        } else {
            const newRoute = nextAuthorizedRoute(to, userType);
            if (newRoute.name !== to.name) {
                return next(newRoute);
            }
        }
    }

    next();
});

export default router;

export const nextAuthorizedRoute: (route: Route, userType?: UserType) => Location = (route, userType) => {
    const nextRoute = route.matched.slice().reverse().find(it => {
        const nextMode = it.meta.requiredLogin as LoginMode | undefined;
        if (nextMode === undefined) {
            return true;
        } else switch (nextMode) {
            case LoginMode.NONE:
                return userType === undefined;
            case LoginMode.MEMBER:
                return userType === UserType.MEMBER;
            case LoginMode.ADMIN:
                return userType === UserType.ADMIN;
            case LoginMode.SOME:
                return userType === UserType.MEMBER || userType === UserType.ADMIN;
        }
    });

    if (nextRoute) {
        return {name: nextRoute.name, params: route.params};
    } else {
        return {name: 'home'};
    }
};
