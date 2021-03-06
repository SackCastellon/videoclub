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
import {User} from '@/data/User';
import {Member, MemberUpdate} from '@/data/Member';
import {Admin, AdminUpdate} from '@/data/Admin';
import {strip} from '@/util/Strip';
import {converter} from '@/util/JsonConverter';

export const getUser = (id: number): Promise<AxiosResponse<User>> =>
    api.get(`user/${id}`);

export const getUsers = (): Promise<AxiosResponse<Array<User>>> =>
    api.get(`user`);

export const getCurrentUser = (): Promise<AxiosResponse<{ user: User, member?: Member, admin?: Admin }>> =>
    api.get('user/current');

export const patchMember = (member: MemberUpdate): Promise<AxiosResponse> =>
    api.patch('user', converter.serializeObject(strip(member, MemberUpdate)));

export const patchAdmin = (admin: AdminUpdate): Promise<AxiosResponse> =>
    api.patch('user', converter.serializeObject(strip(admin, AdminUpdate)));
