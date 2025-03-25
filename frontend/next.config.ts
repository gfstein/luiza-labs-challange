import type {NextConfig} from "next";

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "fakestoreapi.com",
        pathname: "**"
      },
      {
        protocol: "https",
        hostname: "upload.wikimedia.org",
        pathname: "**"
      }
    ]
  }
};

export default nextConfig;
