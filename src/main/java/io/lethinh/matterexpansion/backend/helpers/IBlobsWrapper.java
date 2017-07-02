/**
 * Copyright (C) 2017 Le Thinh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.lethinh.matterexpansion.backend.helpers;

import java.io.IOException;

import io.lethinh.matterexpansion.network.EligiblePacketBuffer;

/**
 * By implementing this class, it allows retrieving the data from the blobs and
 * writing data to it. And the data (blobs tickets) is automatically saved
 * (constructed by classes) before it is cleared (the blobs tickets doesn't sign
 * any more).
 *
 * @author Le Thinh
 */
public interface IBlobsWrapper {

	/**
	 * Load the blobs tickets from the packet.
	 *
	 * @param packet
	 *            The packet needs to load the blobs.
	 */
	void loadBlobsTickets(EligiblePacketBuffer packet) throws IOException;

	/**
	 * Save the blobs tickets to the packet.
	 *
	 * @param packet
	 *            The packet needs to save the blobs.
	 */
	void saveBlobsTickets(EligiblePacketBuffer packet) throws IOException;

}
