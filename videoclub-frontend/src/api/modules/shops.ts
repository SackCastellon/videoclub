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
import {Shop, ShopNew, ShopUpdate} from '@/data/Shop';
import {strip} from '@/util/Strip';
import {converter} from '@/util/JsonConverter';

export const getShop = (id: number): Promise<AxiosResponse<Shop>> =>
    api.get(`shop/${id}`);

export const getShops = (): Promise<AxiosResponse<Array<Shop>>> =>
    api.get('shop');

export const postShop = (shop: ShopNew): Promise<AxiosResponse<{ shopId: number }>> =>
    api.post('shop', converter.serializeObject(strip(shop, ShopNew)));

export const patchShop = (id: number, shop: ShopUpdate): Promise<AxiosResponse> =>
    api.patch(`shop/${id}`, converter.serializeObject(strip(shop, ShopUpdate)));
