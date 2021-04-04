import { FeatureType } from "@/twitch/feature/type/FeatureTypeEnum.ts";

export default interface Feature {
  type: FeatureType;
  id: string;
  editable: boolean;
}
