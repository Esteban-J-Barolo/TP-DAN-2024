/** @type {import('next').NextConfig} */
const nextConfig = {
  output: 'standalone',
  basePath: '/ui',
  plugins: {
    tailwindcss: {},
  },
};
export default nextConfig;