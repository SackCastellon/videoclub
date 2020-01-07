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
import {Stat, StatNew} from '@/data/Stat';
import {strip} from '@/util/Strip';
import {converter} from '@/util/JsonConverter';

export const getStat = (id: number): Promise<AxiosResponse<Stat>> =>
    api.get(`stat/${id}`);

export const getStats = (): Promise<AxiosResponse<Array<Stat>>> =>
    api.get('stat');

export const postStat = (stat: StatNew): Promise<AxiosResponse<{ statId: number }>> =>
    api.post('stat', converter.serializeObject(strip(stat, StatNew)));

