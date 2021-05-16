import Overlay from "@/overlay/Overlay";

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
  list(): Overlay[] {
    return overlays;
  },
  get(id: string): Overlay | undefined {
    return overlays.find(o => o.id === id);
  }
};
