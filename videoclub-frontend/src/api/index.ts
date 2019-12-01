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

import axios from 'axios';
import {UserModule} from '@/store/modules/user';

const api = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
});

api.interceptors.request.use((config) => {
    const token = UserModule.token;
    if (token != null) {
        config.headers.Authorization = `Bearer ${UserModule.token}`;
    }
    return config;
});

export default api;
