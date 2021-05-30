import Overlay from "@/overlay/Overlay";
import axios from "axios";

const overlays = [
  {
    id: "overlay1",
    title: "Overlay 1",
    resolution: { width: 1920, height: 1080 },
    elements: []
  },
  {
    id: "overlay2",
    title: "Overlay 2",
    resolution: { width: 800, height: 600 },
    elements: []
  }
];

export default {
  /*list(): Promise<Overlay[]> {
    return axios.get("http://localhost:8080/overlays").then(response => {
      return response.data as Overlay[];
    });
  },
  async get(id: string): Promise<Overlay> {
    return await axios.get("http://localhost:8080/overlays").then(response => {
      return response.data.find((o: Overlay) => o.id === id) as Overlay;
    });
  }*/
  list(): Overlay[] {
    return overlays;
  },
  get(id: string): Overlay | undefined {
    return overlays.find(o => o.id === id);
  }
};
