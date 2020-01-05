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
import {Rental, RentalNew, RentalUpdate} from '@/data/Rental';

export const getRental = (id: number): Promise<AxiosResponse<Rental>> =>
    api.get(`rental/${id}`);

export const postRental = (rental: RentalNew): Promise<AxiosResponse<{ rentalId: number }>> =>
    api.post('rental', rental);

export const patchRental = (id: number, rental: RentalUpdate): Promise<AxiosResponse> =>
    api.post(`rental/${id}`, rental);
