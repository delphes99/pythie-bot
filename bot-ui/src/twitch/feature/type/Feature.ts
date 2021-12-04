import { FeatureType } from "@/twitch/feature/type/FeatureTypeEnum";

export default interface Feature {
  type: FeatureType;
  id: string;
  editable: boolean;
}
