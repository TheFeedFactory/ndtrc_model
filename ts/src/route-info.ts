import { z } from "zod";
import { AddressSchema } from "./address.js";
import { FileSchema } from "./file.js";
import { TRCItemDetailSchema } from "./trc-item-detail.js";

export const RouteTypeSchema = z.enum([
  "driving_traffic", "driving", "walking", "cycling",
]);

export type RouteType = z.infer<typeof RouteTypeSchema>;

export const RouteInfoTypeSchema = z.enum([
  "eventConnectors", "route_maker", "route_iq", "odp_routes", "other",
]);

export type RouteInfoType = z.infer<typeof RouteInfoTypeSchema>;

export const RouteDifficultySchema = z.enum([
  "easy", "moderate", "challenging", "difficult", "expert",
]);

export type RouteDifficulty = z.infer<typeof RouteDifficultySchema>;

export const SurfaceTypeSchema = z.enum([
  "paved", "gravel", "dirt", "sand", "grass", "cobblestone",
  "boardwalk", "rock", "snow", "water", "mixed",
]);

export type SurfaceType = z.infer<typeof SurfaceTypeSchema>;

export const PoiCategorySchema = z.enum([
  "museum", "monument", "castle", "church", "nature_area",
  "parking", "toilet", "rest_area", "picnic_area",
  "hotel", "camping", "hostel", "bed_breakfast",
  "restaurant", "cafe", "bar", "bakery",
  "swimming", "hiking_start", "cycling_start", "boat_rental",
  "bus_stop", "train_station", "ferry", "bike_rental",
  "viewpoint", "information_point", "other",
]);

export type PoiCategory = z.infer<typeof PoiCategorySchema>;

export const LatLngSchema = z
  .object({
    lat: z.number().optional(),
    lng: z.number().optional(),
    altitude: z.number().optional(),
    label: z.string().optional(),
  })
  .passthrough();

export type LatLng = z.infer<typeof LatLngSchema>;

const LocationItemRefSchema = z
  .object({
    id: z.string().optional(),
    trcid: z.string().optional(),
    text: z.string().optional(),
  })
  .passthrough();

const LocationRefSchema = z
  .object({
    address: AddressSchema.optional(),
    label: z.string().optional(),
    locationItem: LocationItemRefSchema.optional(),
    venueItem: LocationItemRefSchema.optional(),
  })
  .passthrough();

const CalendarRefSchema = z.object({}).passthrough();

export const PoiSchema = z
  .object({
    distanceInKilometersFromStart: z.number().optional(),
    durationInMinutesFromStart: z.number().int().optional(),
    coordinate: LatLngSchema.optional(),
    location: LocationRefSchema.optional(),
    label: z.string().optional(),
    icon: z.string().optional(),
    category: PoiCategorySchema.optional(),
    trcItemDetails: z.array(TRCItemDetailSchema).optional(),
    files: z.array(FileSchema).optional(),
    calendar: CalendarRefSchema.optional(),
    locationItem: LocationItemRefSchema.optional(),
  })
  .passthrough();

export type Poi = z.infer<typeof PoiSchema>;

export const RouteInfoSchema = z
  .object({
    type: RouteInfoTypeSchema.optional(),
    routeType: RouteTypeSchema.optional(),
    url: z.string().optional(),
    distanceInKilometers: z.number().optional(),
    durationInMinutes: z.number().int().optional(),
    start: AddressSchema.optional(),
    end: AddressSchema.optional(),
    pois: z.array(PoiSchema).optional(),
    routeCoordinates: z.array(LatLngSchema).optional(),
    calculatedCoordinates: z.array(LatLngSchema).optional(),
    difficulty: RouteDifficultySchema.optional(),
    primarySurface: SurfaceTypeSchema.optional(),
  })
  .passthrough();

export type RouteInfo = z.infer<typeof RouteInfoSchema>;
