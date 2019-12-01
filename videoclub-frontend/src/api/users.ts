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

import {ICredentials, IRegistrationInfo} from '@/store/modules/user';
import {AxiosResponse} from 'axios';
import api from '@/api';

interface LoginResponse {
    token: string;
    username: string;
}

export const register = (registrationInfo: IRegistrationInfo): Promise<AxiosResponse> =>
    api.post('auth/register', registrationInfo);

export const login = (credentials: ICredentials): Promise<AxiosResponse<LoginResponse>> =>
    api.post<LoginResponse>('auth/login', credentials);

export const refresh = (): Promise<AxiosResponse<LoginResponse>> =>
    api.get<LoginResponse>('auth/refresh');