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

import {AxiosResponse} from 'axios';
import api from '@/api';
import {AuthModule} from '@/store/modules/auth';
import {UserModule} from '@/store/modules/user';
import {MemberNew} from '@/data/Member';


const refreshWindow = 5000;
const minRefreshTimeout = 30000;
let timeoutID: number;


export const register = (registrationInfo: UserRegistration): Promise<AxiosResponse> =>
    api.post('auth/register', registrationInfo);

export const login = async (loginInfo: UserCredential): Promise<void> => {
    try {
        const response = await api.post('auth/login', loginInfo);
        const lifespan: number = response.data.lifespan;
        const timeout = Math.max(minRefreshTimeout, lifespan - refreshWindow);
        timeoutID = window.setTimeout(refresh, timeout);
        AuthModule.setAuthenticated(true);
    } catch (error) {
        AuthModule.setAuthenticated(false);
    } finally {
        await UserModule.load();
    }
};

export const refresh = async (): Promise<void> => {
    try {
        window.clearTimeout(timeoutID);
        const response = await api.get('auth/refresh');
        const lifespan: number = response.data.lifespan;
        const timeout = Math.max(minRefreshTimeout, lifespan - refreshWindow);
        timeoutID = window.setTimeout(refresh, timeout);
        AuthModule.setAuthenticated(true);
    } catch (error) {
        AuthModule.setAuthenticated(false);
    } finally {
        await UserModule.load();
    }
};

export const logout = async (): Promise<void> => {
    try {
        window.clearTimeout(timeoutID);
        await api.get('auth/logout');
        AuthModule.setAuthenticated(false);
    } finally {
        await UserModule.load();
    }
};


export interface UserCredential {
    username: string;
    password: string;
}

export interface UserRegistration {
    member: MemberNew,
    credential: UserCredential
}
