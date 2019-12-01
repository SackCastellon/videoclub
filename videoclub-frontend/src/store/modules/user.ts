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
import {clearToken, getToken, isOnlySession, setToken} from '@/util/jwt';
import store from '@/store';
import {login, refresh} from '@/api/users';

interface IUserState {
    token: string | null;
    username: string | null;
    isAuthenticated: boolean;
}

export interface ICredentials {
    username: string;
    password: string;
}

export interface ILoginInfo {
    username: string;
    password: string;
    rememberToken: boolean;
}

export interface IRegistrationInfo {
    username: string;
    password: string;
    name: string;
    age: number;
}

@Module({dynamic: true, store, name: 'user'})
class User extends VuexModule implements IUserState {
    public token: string | null = getToken();
    public username: string | null = null;
    public isAuthenticated: boolean = false;

    @MutationAction({mutate: ['token', 'username', 'isAuthenticated']})
    public async login(loginInfo: ILoginInfo) {
        const {data} = await login({username: loginInfo.username, password: loginInfo.password});
        const {token, username} = data;

        setToken(token, loginInfo.rememberToken);

        return {
            token,
            username,
            isAuthenticated: true,
        };
    }

    @MutationAction({mutate: ['token', 'username', 'isAuthenticated']})
    public async logout() {
        clearToken();

        return {
            token: null,
            username: null,
            isAuthenticated: false,
        };
    }

    @MutationAction({mutate: ['token', 'username', 'isAuthenticated']})
    public async refresh() {
        console.debug('token=' + UserModule.token);
        if (UserModule.token != null && !UserModule.isAuthenticated) {
            const rememberToken = !isOnlySession();

            const {data} = await refresh();
            const {token, username} = data;

            setToken(token, rememberToken);

            return {
                token,
                username,
                isAuthenticated: true,
            };
        } else {
            clearToken();

            return {
                token: null,
                username: null,
                isAuthenticated: false,
            };
        }
    }
}

export const UserModule = getModule(User);
