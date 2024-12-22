'use client';

import {
  UserGroupIcon,
  HomeIcon,
  WalletIcon,
  Squares2X2Icon
} from '@heroicons/react/24/outline';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import clsx from 'clsx';

// Map of links to display in the side navigation.
// Depending on the size of the application, this would be stored in a database.
const links = [
  { name: 'Cuenta', href: '/dashboard', icon: HomeIcon },
  {
    name: 'Clientes',
    href: '/dashboard/clientes',
    icon: UserGroupIcon,
  },
  { name: 'Productos', href: '/dashboard/productos', icon: Squares2X2Icon },
  { name: 'Pedidos', href: '/dashboard/pedidos', icon: WalletIcon },
];

export default function NavLinks() {
  const pathname = usePathname();

  return (
    <>
      {links.map((link) => {
        const LinkIcon = link.icon;
        return (
          <Link
            key={link.name}
            href={link.href}
            className={clsx(
              'flex bg-red-900 h-[48px] grow items-center justify-center gap-2 rounded-md p-3 text-sm font-medium hover:bg-sky-100 hover:text-blue-600 md:flex-none md:justify-start md:p-2 md:px-3',
              {
                'bg-red-500 text-yellow-300': pathname === link.href,
              },
            )}
          >
            <LinkIcon className="w-6" />
            <p className="hidden md:block">{link.name}</p>
          </Link>
        );
      })}
    </>
  );
}