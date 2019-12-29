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

import {getModule, Module, MutationAction, VuexModule} from 'vuex-module-decorators';
import {clearToken, getToken, setToken} from '@/util/xsrf';
import store from '@/store';
import {login, logout, refresh} from '@/api/auth';

interface IAuthState {
    xsrfToken: string | null;
    isAuthenticated: boolean;
}

export interface ICredentials {
    username: string;
    password: string;
}

export interface ILoginInfo {
    username: string;
    password: string;
}

export interface IRegistrationInfo {
    username: string;
    password: string;
    name: string;
    age: number;
}

@Module({dynamic: true, store, name: 'auth'})
class Auth extends VuexModule implements IAuthState {
    public xsrfToken: string | null = getToken();
    public isAuthenticated: boolean = false;

    @MutationAction({mutate: ['xsrfToken', 'isAuthenticated']})
    public async login(loginInfo: ILoginInfo): Promise<IAuthState> {
        const response = await login({username: loginInfo.username, password: loginInfo.password});
        const xsrfToken = response.headers["xsrf-token"];

        setToken(xsrfToken);

        return {
            xsrfToken,
            isAuthenticated: true,
        };
    }

    @MutationAction({mutate: ['xsrfToken', 'isAuthenticated']})
    public async logout(): Promise<IAuthState> {
        await logout();

        clearToken();

        return {
            xsrfToken: null,
            isAuthenticated: false,
        };
    }

    @MutationAction({mutate: ['xsrfToken', 'isAuthenticated']})
    public async refreshToken(): Promise<IAuthState> {
        if (AuthModule.xsrfToken != null && !AuthModule.isAuthenticated) {
            const response = await refresh();
            const xsrfToken = response.headers["xsrf-token"];

            setToken(xsrfToken);

            return {
                xsrfToken,
                isAuthenticated: true,
            };
        } else {
            clearToken();

            return {
                xsrfToken: null,
                isAuthenticated: false,
            };
        }
    }
}

export const AuthModule = getModule(Auth);