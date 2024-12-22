import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  output: 'standalone',
  basePath: '/ui',
  // async rewrites() {
  //   return [
  //     {
  //       source: '/api/:path*', // Proxy para todas las llamadas a /api/*
  //       destination: 'http://haproxy:80/api/:path*', // Redirige a HAProxy
  //     },
  //     {
  //       source: '/clientes/:path*', // Proxy específico para clientes
  //       destination: 'http://haproxy:80/clientes/:path*',
  //     },
  //     {
  //       source: '/productos/:path*', // Proxy específico para productos
  //       destination: 'http://haproxy:80/productos/:path*',
  //     },
  //     {
  //       source: '/pedidos/:path*', // Proxy específico para pedidos
  //       destination: 'http://haproxy:80/pedidos/:path*',
  //     },
  //   ];
  // },
};

export default nextConfig;
