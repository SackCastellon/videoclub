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
import api from '@/api/index';
import {Shop} from '@/data/Shop';

export const getShops = (): Promise<AxiosResponse<Array<Shop>>> =>
    api.get<Array<Shop>>('shop');

export const getShop = (id: number): Promise<AxiosResponse<Shop>> =>
    api.get<Shop>(`shop/${id}`);

export const postShop = (shop: Shop): Promise<AxiosResponse> =>
    api.post('shop', shop);
