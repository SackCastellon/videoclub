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

import {AxiosResponse} from 'axios';
import api from '@/api';
import {AuthModule} from '@/store/modules/auth';


const refreshWindow = 5000;
const minRefreshTimeout = 30000;
let timeoutID: number;


export const register = async (registrationInfo: IRegistrationInfo): Promise<AxiosResponse> => {
    return await api.post('auth/register', registrationInfo);
};

export const login = async (loginInfo: ILoginInfo): Promise<AxiosResponse> => {
    try {
        const response = await api.post('auth/login', loginInfo);
        const lifespan: number = response.data.lifespan;
        const timeout = Math.max(minRefreshTimeout, lifespan - refreshWindow);
        timeoutID = window.setTimeout(refreshToken, timeout);
        AuthModule.setAuthenticated(true);
        return response;
    } catch (error) {
        AuthModule.setAuthenticated(false);
        throw error;
    }
};

export const refreshToken = async (): Promise<AxiosResponse> => {
    try {
        window.clearTimeout(timeoutID);
        const response = await api.get('auth/refresh');
        const lifespan: number = response.data.lifespan;
        const timeout = Math.max(minRefreshTimeout, lifespan - refreshWindow);
        timeoutID = window.setTimeout(refreshToken, timeout);
        AuthModule.setAuthenticated(true);
        return response;
    } catch (error) {
        AuthModule.setAuthenticated(false);
        throw error;
    }
};

export const logout = async (): Promise<AxiosResponse> => {
    window.clearTimeout(timeoutID);
    const response = await api.get('auth/logout');
    AuthModule.setAuthenticated(false);
    return response;
};


export interface ILoginInfo {
    username: string;
    password: string;
}

export interface IRegistrationInfo {
    member: {
        name: string;
        age: number;
    },
    credential: {
        username: string;
        password: string;
    }
}
