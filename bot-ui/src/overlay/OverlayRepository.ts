import Overlay from "@/overlay/Overlay.ts";
import axios from "axios";

export default {
  list(): Promise<Overlay[]> {
    return axios.get("http://localhost:8080/overlays").then(response => {
      return response.data as Overlay[];
    });
  },
  async get(id: string): Promise<Overlay> {
    return await axios.get("http://localhost:8080/overlays").then(response => {
      return response.data.find((o: Overlay) => o.id === id) as Overlay;
    });
  }
};
